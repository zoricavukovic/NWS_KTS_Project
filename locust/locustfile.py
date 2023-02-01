import random

from locust import HttpUser, task, events, between
import requests


@events.init.add_listener
def on_locust_init(environment, **_kwargs):
    print('Ran locust script')


class QuickStartUser(HttpUser):
    vehicles_dict_with_coordinates = {}
    vehicle_chosen_route_idx = {}
    host = "http://localhost:8080"
    wait_time = between(0.5, 2)

    def on_start(self):
        self.vehicles_dict = {}
        self.vehicles = requests.get('http://localhost:8080/vehicles/locust').json()

        for vehicle in self.vehicles:
            vehicleId = vehicle["vehicleId"]

            if vehicleId not in self.vehicles_dict:
                self.vehicles_dict[vehicleId] = vehicle
                self.get_coordinates(vehicle)

    def get_coordinates(self, vehicle):
        if vehicle["crossedWaypoints"] == -1 or not vehicle["crossedWaypoints"] < (len(vehicle["waypoints"])-1) \
                or not vehicle["activeDriver"] or not vehicle["inDrive"]:

            return
        first_point = vehicle["waypoints"][vehicle["crossedWaypoints"]] #izabere lokaciju iz waypoints
        second_point = vehicle["waypoints"][vehicle["crossedWaypoints"] + 1]  # izabere lokaciju iz waypoints + 1

        response = requests.get(
                f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{first_point["lng"]},{first_point["lat"]};{second_point["lng"]},{second_point["lat"]}?geometries=geojson&overview=false&alternatives=true&steps=true')

        routeGeoJSON = response.json()


        chosenRouteIdx = vehicle["chosenRouteIdx"][vehicle["crossedWaypoints"]]
        coordinates = []
        randomNum = random.randint(1, 50)

        if randomNum > 30 and len(routeGeoJSON['routes']) >= 2:
            chosenRouteIdx = 0 if chosenRouteIdx > 0 else 1

        self.vehicle_chosen_route_idx[vehicle['vehicleId']] = chosenRouteIdx
        for step in routeGeoJSON['routes'][chosenRouteIdx]['legs'][0]['steps']:
            coordinates += [*step['geometry']['coordinates']]

        coordinates += [[second_point["lng"], second_point["lat"]]]

        self.vehicles_dict_with_coordinates[vehicle["vehicleId"]] = coordinates

    @task
    def update_coordinate(self):
        for vehicle in self.vehicles_dict.values():

            if not vehicle['vehicleId'] in self.vehicle_chosen_route_idx.keys():
                self.vehicle_chosen_route_idx[vehicle['vehicleId']] = -1
            responseCheck = self.client.get(f"/vehicles/check-vehicle-activity/{vehicle['vehicleId']}/{self.vehicle_chosen_route_idx[vehicle['vehicleId']]}")

            responseCheckStatusJSON = responseCheck.json()

            if vehicle["activeDriver"] != responseCheckStatusJSON["activeDriver"] or vehicle["inDrive"] != responseCheckStatusJSON["inDrive"] \
                    or not vehicle["activeDriver"] or not vehicle["inDrive"] or not self.listsAreSame(vehicle["waypoints"], responseCheckStatusJSON["waypoints"]):

                vehicle["activeDriver"] = responseCheckStatusJSON["activeDriver"]
                vehicle["inDrive"] = responseCheckStatusJSON["inDrive"]
                vehicle["waypoints"] = responseCheckStatusJSON["waypoints"]
                vehicle["activeDriver"] = responseCheckStatusJSON["activeDriver"]
                vehicle["chosenRouteIdx"] = responseCheckStatusJSON["chosenRouteIdx"]
                vehicle["currentIndexOfLocation"] = responseCheckStatusJSON["currentIndexOfLocation"]
                vehicle["crossedWaypoints"] = responseCheckStatusJSON["crossedWaypoints"]

                self.get_coordinates(vehicle)


            if vehicle["vehicleId"] in self.vehicles_dict_with_coordinates and len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) > 0: #ima jos koordinata na tom waypointu

                new_coordinate = self.vehicles_dict_with_coordinates[vehicle["vehicleId"]].pop(0)

                response = self.client.put(f"/vehicles/update-current-location/{vehicle['vehicleId']}", json={
                    'longLatRequest': {
                        'lat': new_coordinate[1],
                        'lon': new_coordinate[0]
                    },
                    'crossedWaypoints': vehicle["crossedWaypoints"],
                    'chosenRouteIdx': self.vehicle_chosen_route_idx[vehicle['vehicleId']]
                })
                responseJSON = response.json()
                vehicle["activeDriver"] = responseJSON["activeDriver"]
                vehicle["inDrive"] = responseJSON["inDrive"]
            elif vehicle["vehicleId"] in self.vehicles_dict_with_coordinates and len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) == 0: #nema koordinata na tom waypointu

                if vehicle["crossedWaypoints"]+1 < len(vehicle["waypoints"])-1:
                    vehicle["crossedWaypoints"] += 1

                    self.vehicles_dict[vehicle["vehicleId"]] = vehicle

                    self.get_coordinates(vehicle)
                    if len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) > 0:
                        new_coordinate = self.vehicles_dict_with_coordinates[vehicle["vehicleId"]].pop(0)
                        response = self.client.put(f"/vehicles/update-current-location/{vehicle['vehicleId']}", json={
                            'longLatRequest': {
                                'lat': new_coordinate[1],
                                'lon': new_coordinate[0]
                            },
                            'crossedWaypoints': vehicle["crossedWaypoints"],
                            'chosenRouteIdx': self.vehicle_chosen_route_idx[vehicle['vehicleId']]
                        })


                        responseJSON = response.json()
                        vehicle["activeDriver"] = responseJSON["activeDriver"]
                        vehicle["inDrive"] = responseJSON["inDrive"]

    def listsAreSame(self, list1, list2):

        if len(list1) != len(list2):

            return False
        for i in range(0, len(list1)):
            if list1[i]["lat"] != list2[i]["lat"] or list1[i]["lng"] != list2[i]["lng"]:

                return False

        return True

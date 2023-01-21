from locust import HttpUser, task, events, between
import requests


@events.init.add_listener
def on_locust_init(environment, **_kwargs):
    print('Ran locust script')


class QuickStartUser(HttpUser):
    vehicles_dict_with_coordinates = {}
    host = "http://localhost:8080"
    wait_time = between(0.5, 2)

    def on_start(self):
        self.vehicles_dict = {}
        self.vehicles = requests.get('http://localhost:8080/vehicles/locust').json()

        for vehicle in self.vehicles:
            vehicleId = vehicle["vehicleId"]

            if vehicleId not in self.vehicles_dict:
                # print("USAO DA TRAZI KOORDINATE")
                self.vehicles_dict[vehicleId] = vehicle
                self.get_coordinates(vehicle)
                # print(self.vehicles_dict_with_coordinates)

    def get_coordinates(self, vehicle):
        # print("waypoint")
        if vehicle["crossedWaypoints"] == -1 or not vehicle["activeDriver"] or not vehicle["inDrive"]:

            return
        crossedNum = 0
        # print("chosed")
        # print(vehicle["chosenRouteIdx"])
        first_point = vehicle["waypoints"][vehicle["crossedWaypoints"]] #izabere lokaciju iz waypoints
        second_point = vehicle["waypoints"][vehicle["crossedWaypoints"] + 1]  # izabere lokaciju iz waypoints + 1
        # print(first_point)
        # print("\n")
        # print(second_point)
        response = requests.get(
                f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{first_point["lng"]},{first_point["lat"]};{second_point["lng"]},{second_point["lat"]}?geometries=geojson&overview=false&alternatives=true&steps=true')
        # print("\n============ISPIST=========\n\n")

        routeGeoJSON = response.json()
        # print(routeGeoJSON)
        # print("\n============ISPIS KRAJ=========\n\n")

        chosenRouteIdx = vehicle["chosenRouteIdx"][vehicle["crossedWaypoints"]]
        # print("\n============ISPISA IZABRANOG INDEKSA RUTE=========\n\n")
        # print(chosenRouteIdx)
        # print(routeGeoJSON['routes'][0]['legs'])
        # print("\n============KRAJ ISPISA IZABRANOG INDEKSA RUTE=========\n\n")
        coordinates = []
        for step in routeGeoJSON['routes'][0]['legs'][chosenRouteIdx]['steps']:
            coordinates += [*step['geometry']['coordinates']]
        self.vehicles_dict_with_coordinates[vehicle["vehicleId"]] = coordinates

    @task
    def update_coordinate(self):
        for vehicle in self.vehicles_dict.values():
            # print("UPDATEJU SE KOORDINATE")
            # print(vehicle)
            if not vehicle["activeDriver"] or not vehicle["inDrive"]:
                response = self.client.get(f"/vehicles/check-vehicle-activity/{vehicle['vehicleId']}")

                responseJSON = response.json()
                vehicle["activeDriver"] = responseJSON["activeDriver"]
                vehicle["inDrive"] = responseJSON["inDrive"]
                self.get_coordinates(vehicle)

            if len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) > 0: #ima jos koordinata na tom waypointu
                new_coordinate = self.vehicles_dict_with_coordinates[vehicle["vehicleId"]].pop(0)
                response = self.client.put(f"/vehicles/update-current-location/{vehicle['vehicleId']}", json={
                    'longLatRequest': {
                        'lat': new_coordinate[1],
                        'lon': new_coordinate[0]
                    },
                    'crossedWaypoints': vehicle["crossedWaypoints"]
                })
                responseJSON = response.json()
                vehicle["activeDriver"] = responseJSON["activeDriver"]
                vehicle["inDrive"] = responseJSON["inDrive"]
            elif len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) == 0: #nema koordinata na tom waypointu
                # print("UPDATE COORDINATE KAD IH NEMA VISE U DATOM WAYPOINTU\n")
                # print(self.vehicles_dict[vehicle["vehicleId"]])
                if vehicle["crossedWaypoints"]+1 < len(vehicle["waypoints"])-1:

                    vehicle["crossedWaypoints"] += 1
                    # print(vehicle)
                    self.vehicles_dict[vehicle["vehicleId"]] = vehicle
                    # print(self.vehicles_dict[vehicle["vehicleId"]])
                    # print("UPDATE COORDINATE ZAVRSEN KAD IH NEMA VISE U DATOM WAYPOINTU\n")
                    self.get_coordinates(vehicle)
                    if len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) > 0:
                        new_coordinate = self.vehicles_dict_with_coordinates[vehicle["vehicleId"]].pop(0)
                        response = self.client.put(f"/vehicles/update-current-location/{vehicle['vehicleId']}", json={
                            'longLatRequest': {
                                'lat': new_coordinate[1],
                                'lon': new_coordinate[0]
                            },
                            'crossedWaypoints': vehicle["crossedWaypoints"]
                        })


                        responseJSON = response.json()
                        vehicle["activeDriver"] = responseJSON["activeDriver"]
                        vehicle["inDrive"] = responseJSON["inDrive"]


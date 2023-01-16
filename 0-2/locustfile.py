from datetime import time

from locust import HttpUser, TaskSet, task, constant, events, between
import json
import requests

# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
#     vehicle = requests.get('http://localhost:8080/vehicles/active')

@events.init.add_listener
def on_locust_init(environment, **_kwargs):
    print('Ran init')
    # vehicles = requests.get('http://localhost:8080/vehicles/active')


class QuickstartUser(HttpUser):
    vehicles_dict_with_coordinates = {}
    host = "http://localhost:8080"
    wait_time = between(0.5, 2)

    def on_start(self):
        self.vehicles_dict = {}
        self.vehicles = requests.get('http://localhost:8080/vehicles/active/locust').json()
        print("\n\n--------------------------------------\n\n\n\n\n")
        # print(self.vehicles)
        for vehicle in self.vehicles:
            print(vehicle)
            print("---------------------------------")
            print(self.vehicles_dict)
            print("---------------------------------")
            print(self.vehicles_dict_with_coordinates)
            vehicleId = vehicle["vehicleId"]

            if vehicleId not in self.vehicles_dict:
                print("USAO DA TRAZI KOORDINATE")
                self.vehicles_dict[vehicleId] = vehicle
                self.get_coordinates(vehicle)

    # self.vehicles su sva vozila ciji su vozaci trenutno aktivni i imaju svoj id
    # self.vehicle = [ {  id:1, inDrive:True, currentLocationIndex:0   } ]
    # @task
    # def get_users(self):
    #     self.vehicles_dict = {}
    #     self.vehicles = requests.get('http://localhost:8080/vehicles/active/locust').json()
    #     print("\n\n--------------------------------------\n\n\n\n\n")
    #     # print(self.vehicles)
    #     for vehicle in self.vehicles:
    #         print(vehicle)
    #         print("---------------------------------")
    #         print(self.vehicles_dict)
    #         print("---------------------------------")
    #         print(self.vehicles_dict_with_coordinates)
    #         vehicleId = vehicle["vehicleId"]
    #
    #         if vehicleId in self.vehicles_dict:
    #             self.update_coordinate(vehicle)
    #         else:
    #             print("USAO DA TRAZI KOORDINATE")
    #             self.vehicles_dict[vehicleId] = vehicle
    #             self.get_coordinates(vehicle)
    #
    #     # res = self.client.put("/vehicles/update-current-location")
    #     # print(self.vehicles)
    #     # print(res.text)
    #     # print(res.content)
    #     # print(res.status_code)


    def get_coordinates(self, vehicle):
        print("waypoint")
        if vehicle["crossedWaypoints"] == -1:
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
            # self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]



        # for index in range(0, len(vehicle["chosenRouteIdx"]), 2):
        #     print(index)
        #     print(vehicle["crossedWaypoints"][index])
        #     print(crossedNum)
            # if crossedNum == vehicle["crossedWaypoints"]:
            #     chosenRouteIndex = vehicle["chosenRouteIdx"][index]
            #     response = requests.get(
            #         f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{self.departure[1]},{self.departure[0]};{self.destination[1]},{self.destination[0]}?geometries=geojson&overview=false&alternatives=true&steps=true')
            #     self.routeGeoJSON = response.json()
            #     self.coordinates = []
            #     for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
            #         self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]
            #     self.ride = self.client.post('/api/ride', json={
            #         'routeJSON': json.dumps(self.routeGeoJSON),
            #         'rideStatus': 0,
            #         'vehicle': {
            #             'id': self.vehicle['id'],
            #             'licensePlateNumber': self.vehicle['licensePlateNumber'],
            #             'latitude': self.coordinates[0][0],
            #             'longitude': self.coordinates[0][1]
            #         }
            #     }).json()

            # crossedNum+=1



    @task
    def update_coordinate(self):
        for vehicle in self.vehicles_dict.values():
            print("UPDATEJU SE KOORDINATE")
            print(vehicle)
            if len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) > 0: #ima jos koordinata na tom waypointu
                new_coordinate = self.vehicles_dict_with_coordinates[vehicle["vehicleId"]].pop(0)
                self.client.put(f"/vehicles/update-current-location/{vehicle['vehicleId']}", json={
                    'lat': new_coordinate[0],
                    'lon': new_coordinate[1]
                })
            elif len(self.vehicles_dict_with_coordinates[vehicle["vehicleId"]]) == 0: #nema koordinata na tom waypointu
                print("UPDATE COORDINATE KAD IH NEMA VISE U DATOM WAYPOINTU\n")
                print(self.vehicles_dict[vehicle["vehicleId"]])
                vehicle["crossedWaypoints"] += 1
                self.vehicles_dict[vehicle["vehicleId"]] = vehicle
                print(self.vehicles_dict[vehicle["vehicleId"]])
                print("UPDATE COORDINATE ZAVRSEN KAD IH NEMA VISE U DATOM WAYPOINTU\n")
                self.get_coordinates(vehicle)
                new_coordinate = self.vehicles_dict_with_coordinates[vehicle["vehicleId"]].pop(0)
                self.client.put(f"/vehicles/update-current-location/{vehicle['vehicleId']}", json={
                    'lat': new_coordinate[0],
                    'lon': new_coordinate[1]
            })



#
#
# import requests
# import json
#
# from locust import HttpUser, task, between, events
# from random import randrange
#
#
# start_and_end_points = [
#     (45.235866, 19.807387),     # Djordja Mikeša 2
#
# ]
#
#
# taxi_stops = [
#     (45.238548, 19.848225),   # Stajaliste na keju
# ]
#
#
# license_plates = [
#     'NS-001-AA',
#     'NS-001-AC'
# ]

#
# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
#     requests.delete('http://localhost:8080/api/ride')
#     requests.delete('http://localhost:8080/api/vehicle')
#
#
# class QuickstartUser(HttpUser):
#     host = 'http://localhost:8080'
#     wait_time = between(0.5, 2)
#
#     def on_start(self):
#         random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
#         self.vehicle = self.client.post('/api/vehicle', json={
#             'licensePlateNumber': license_plates.pop(0),
#             'latitude': random_taxi_stop[0],
#             'longitude': random_taxi_stop[1]
#         }).json()
#         self.driving_to_start_point = True
#         self.driving_the_route = False
#         self.driving_to_taxi_stop = False
#         self.departure = random_taxi_stop
#         self.destination = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
#         self.get_new_coordinates()
#
#     @task
#     def update_vehicle_coordinates(self):
#         if len(self.coordinates) > 0:
#             new_coordinate = self.coordinates.pop(0)
#             self.client.put(f"/api/vehicle/{self.vehicle['id']}", json={
#                 'latitude': new_coordinate[0],
#                 'longitude': new_coordinate[1]
#             })
#         elif len(self.coordinates) == 0 and self.driving_to_start_point:
#             self.end_ride()
#             self.departure = self.destination
#             while (self.departure[0] == self.destination[0]):
#                 self.destination = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
#             self.get_new_coordinates()
#             self.driving_to_start_point = False
#             self.driving_the_route = True
#         elif len(self.coordinates) == 0 and self.driving_the_route:
#             random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
#             start_and_end_points.append(self.departure)
#             self.end_ride()
#             self.departure = self.destination
#             self.destination = random_taxi_stop
#             self.get_new_coordinates()
#             self.driving_the_route = False
#             self.driving_to_taxi_stop = True
#         elif len(self.coordinates) == 0 and self.driving_to_taxi_stop:
#             random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
#             start_and_end_points.append(self.departure)
#             self.end_ride()
#             self.departure = random_taxi_stop
#             self.destination = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
#             self.get_new_coordinates()
#             self.driving_to_taxi_stop = False
#             self.driving_to_start_point = True
#
#     def get_new_coordinates(self):
#         response = requests.get(f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{self.departure[1]},{self.departure[0]};{self.destination[1]},{self.destination[0]}?geometries=geojson&overview=false&alternatives=true&steps=true')
#         self.routeGeoJSON = response.json()
#         self.coordinates = []
#         for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
#             self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]
#         self.ride = self.client.post('/api/ride', json={
#             'routeJSON': json.dumps(self.routeGeoJSON),
#             'rideStatus': 0,
#             'vehicle': {
#                 'id': self.vehicle['id'],
#                 'licensePlateNumber': self.vehicle['licensePlateNumber'],
#                 'latitude': self.coordinates[0][0],
#                 'longitude': self.coordinates[0][1]
#             }
#         }).json()
#
#     def end_ride(self):
#         self.client.put(f"/api/ride/{self.ride['id']}")

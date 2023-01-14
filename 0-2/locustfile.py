from datetime import time

from locust import HttpUser, TaskSet, task, constant, events, between
import json

# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
#     vehicle = requests.get('http://localhost:8080/vehicles/active')

@events.init.add_listener
def on_locust_init(environment, **_kwargs):
    print('Ran init')
    # vehicles = requests.get('http://localhost:8080/vehicles/active')


class WebsiteUser(HttpUser):
    host = "http://localhost:8080"
    wait_time = between(0.5, 2)

    def on_start(self):
        print("On start")
        self.vehicles = requests.get('http://localhost:8080/vehicles/active').json()
        # print(self.vehicles)

    # self.vehicles su sva vozila ciji su vozaci trenutno aktivni i imaju svoj id
    # self.vehicle = [ {  id:1, inDrive:True, currentLocationIndex:0   } ]
    @task
    def get_users(self):
        self.vehicles_dict = {}
        self.vehicles = requests.get('http://localhost:8080/vehicles/active').json()
        print("\n\n--------------------------------------\n\n\n\n\n")
        print(self.vehicles)
        for vehicle in self.vehicles:
            print(vehicle)
            vehicleId = vehicle["vehicleId"]
            if vehicle["inDrive"]:
                if vehicleId in self.vehicles_dict:
                    self.update_coordinate(vehicle)
                else:
                    self.vehicles_dict[vehicleId] = self.get_coordinates(vehicle)

            else:
                if vehicleId in self.vehicles_dict:
                    del self.vehicles_dict[vehicleId]

        res = self.client.put("/vehicles/update-current-location")
        print(self.vehicles)
        # print(res.text)
        # print(res.content)
        # print(res.status_code)


    def get_coordinates(self, vehicle):
        if vehicle["crossedWaypoints"] == -1:
            return
        crossedNum = 0
        for index in range(0, len(vehicle["chosenRouteIdx"]), 2):
            print(index)
            print(vehicle["crossedWaypoints"][index])
            print(crossedNum)
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

            crossedNum+=1




    def update_coordinate(self, vehicle):
        response = requests.get(f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{self.departure[1]},{self.departure[0]};{self.destination[1]},{self.destination[0]}?geometries=geojson&overview=false&alternatives=true&steps=true')
        self.routeGeoJSON = response.json()
        self.coordinates = []
        for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
            self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]
        self.ride = self.client.post('/api/ride', json={
            'routeJSON': json.dumps(self.routeGeoJSON),
            'rideStatus': 0,
            'vehicle': {
                'id': self.vehicle['id'],
                'licensePlateNumber': self.vehicle['licensePlateNumber'],
                'latitude': self.coordinates[0][0],
                'longitude': self.coordinates[0][1]
            }
        }).json()


import requests
import json

from locust import HttpUser, task, between, events
from random import randrange


start_and_end_points = [
    (45.235866, 19.807387),     # Djordja Mikeša 2

]


taxi_stops = [
    (45.238548, 19.848225),   # Stajaliste na keju
]


license_plates = [
    'NS-001-AA',
    'NS-001-AC'
]

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

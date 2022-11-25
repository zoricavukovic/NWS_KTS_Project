from locust import HttpUser, TaskSet, task, constant
import json





class WebsiteUser(HttpUser):
    host = "http://localhost:8080"
    wait_time = constant(5)

    @task
    def get_users(self):
        res = self.client.put("/vehicles/update-current-location")
        print(res.text)
        print(res.content)
        print(res.status_code)

from locust import HttpUser, TaskSet, task, constant
import json
from locust import User, TaskSet, task

# class DummyTask(TaskSet):
#     host = "http://localhost:8080"
#     @task(1)
#     def dummy(self):
#         pass
#
# class Dummy(User):
#     task_set = DummyTask

class WebsiteUser(HttpUser):
    host = "http://localhost:8080"
    wait_time = constant(5)

    @task
    def get_users(self):
        res = self.client.put("/vehicles/update-current-location")
        print(res.text)
        print(res.content)
        print(res.status_code)

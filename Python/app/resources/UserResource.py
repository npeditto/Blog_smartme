from flask_restful import Resource


class UserRouter(Resource):
    def get(self, user):
        return {"test" : "test"}



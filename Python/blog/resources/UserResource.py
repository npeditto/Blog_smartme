from blog.models.User import User
from blog.decorator.AuthDecorator import authenticated
from blog.exception.ResourceNotFoundException import ResourceNotFoundException

from flask_restful import Resource


pathURI = "/user"


class UserResource(Resource):
    def __new__(cls, *args, **kwargs):
        from blog.hateoas.HATEOASGenerator import SchemaGenerator

        cls.schema = SchemaGenerator.generate(User, UserResource)()

        return super(UserResource, cls).__new__(cls)

    @classmethod
    def relationships(cls):
        return {}

    @staticmethod
    def path():
        return pathURI

    @authenticated
    def get(self, current_user, user_id):
        user = User.query.get(user_id)
        if not user:
            raise ResourceNotFoundException("Utente non trovato.")

        return self.schema.dump(user)


class UserCollection(Resource):
    def __new__(cls, *args, **kwargs):
        from blog.hateoas.HATEOASGenerator import SchemaGenerator

        cls.schema = SchemaGenerator.generate(User, UserResource)(many=True)
        return super(UserCollection, cls).__new__(cls)

    @classmethod
    def relationships(cls):
        return {}

    @staticmethod
    def path():
        return pathURI

    @authenticated
    def get(self, current_user):
        return self.schema.dump(User.query.all())



from flask import jsonify, request
from flask_restful import Resource

from blog.models.Post import Post

from blog.exception.ResourceNotFoundException import ResourceNotFoundException
from blog.models.Database import db
from blog.decorator.AuthDecorator import authenticated

from blog.decorator.ValidateSchema import ValidateSchema
from blog.resources.validation.PostRequestSchema import PostRequestSchema

pathURI = "/post"


class PostResource(Resource):
    def __new__(cls, *args, **kwargs):
        from blog.hateoas.HATEOASGenerator import SchemaGenerator
        cls.schema = SchemaGenerator.generate(Post, PostResource)()
        return super(PostResource, cls).__new__(cls)

    @classmethod
    def relationships(cls):
        from blog.hateoas.Marshmallow import ma

        return {
            "author": ma.URLFor('authorresource.get', values={"post_id": "<post_id>"}),
            "collection": ma.URLFor("postcollection.get")
        }

    @staticmethod
    def path():
        return pathURI

    def get(self, post_id):
        post = Post.query.get(post_id)
        if not post:
            raise ResourceNotFoundException()

        return self.schema.dump(post)

    @authenticated
    def delete(self, current_user, post_id):
        post = Post.query.get(post_id)

        if not post:
            raise ResourceNotFoundException()

        db.session.delete(post)
        db.session.commit()

        return jsonify({"ok" : "Risorsa eliminata."})

    """
        Viene autenticata la richiesta e poi viene verificato lo schema
        della richiesta al fine di permettere di passare solo richieste
    """

    @authenticated
    @ValidateSchema(PostRequestSchema)
    def put(self, current_user, post_id):
        post = Post.query.get(post_id)

        if not post:
            raise ResourceNotFoundException()

        data: dict = request.json

        for key, value in data.items():
            setattr(post, key, value)

        db.session.commit()

        return self.schema.dump(post)

    @authenticated
    @ValidateSchema(PostRequestSchema)
    def post(self, current_user):
        data = request.get_json()

        p = Post(
            contenuto=data["contenuto"],
            autore=current_user.user_id
        )

        db.session.add(p)
        db.session.commit()

        return self.schema.dump(p)


class PostCollection(Resource):
    def __new__(cls, *args, **kwargs):
        from blog.hateoas.HATEOASGenerator import SchemaGenerator

        cls.schema = SchemaGenerator.generate(Post, PostResource)(many=True)
        return super(PostCollection, cls).__new__(cls)

    @staticmethod
    def path():
        return pathURI

    @staticmethod
    def relationships():
        return {}

    def get(self):
        return self.schema.dump(Post.query.all())

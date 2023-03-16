from flask import jsonify, request, make_response

from blog.models.User import User
from blog.models.Post import Post
from blog.models.Database import db
from blog.decorator.AuthDecorator import authenticated

from blog.exception.ResourceNotFoundException import ResourceNotFoundException

from flask_restful import Resource


pathURI = "/post/<post_id>/author"


class AuthorResource(Resource):
    def __new__(cls, *args, **kwargs):
        from blog.hateoas.HATEOASGenerator import SchemaGenerator
        cls.schema = SchemaGenerator.generate(User, AuthorResource)()
        return super(AuthorResource, cls).__new__(cls)

    @classmethod
    def relationships(cls):
        from blog.hateoas.Marshmallow import ma

        return {
            "post": ma.URLFor('postresource.get', values={"post_id": "<post_id>"})
        }

    @staticmethod
    def path():
        return pathURI

    def get(self, post_id):
        post = Post.query.get(post_id)

        if not post:
            raise ResourceNotFoundException()

        author = User.query.get(post.autore)

        if not author:
            return make_response(jsonify({"error": "User not found."}), 400)

        setattr(author, "post_id", post_id)

        return self.schema.dump(author)

    @authenticated
    def patch(self, current_user, post_id):
        post = Post.query.get(post_id)

        if not post:
            raise ResourceNotFoundException()

        try:
            data = request.json
            if "autore" not in data.keys():
                raise Exception("Autore non inserito")
        except:
            return make_response(jsonify({"error": "Inserisci il campo 'autore' per poterlo modificare"}), 400)

        author = User.query.get(data["autore"])

        if not author:
            return make_response(jsonify({"error": "Utente non trovato."}), 400)

        post.autore = author.user_id
        author.post_id = post_id

        db.session.commit()

        return {"ok": "Autore modificato"}

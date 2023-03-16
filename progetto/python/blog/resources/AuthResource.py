import uuid
from os import environ

import datetime
import jwt
from flask import Blueprint, request, jsonify
from sqlalchemy.exc import IntegrityError
from werkzeug.security import generate_password_hash, check_password_hash

from blog.models.Database import db
from blog.models.User import User

from blog.decorator.AuthDecorator import authenticated
from blog.decorator.ValidateSchema import ValidateSchema
from blog.resources.validation.LoginSchema import LoginSchema
from blog.resources.validation.RegisterSchema import RegisterSchema

from blog.models.Token import Token

"""
    Creazione di un Blueprint al fine di impostare una serie di informazioni
    base per le route che si vanno a creare. Questo verrà impiegato per impostare
    le routes di autenticazione come mostrato sotto.
"""
auth = Blueprint("auth_blueprint", "auth_blueprint", url_prefix="/api/v1/auth")

@auth.route("/register", methods=["POST"])
@ValidateSchema(RegisterSchema)
def register():
    """
    Funzione utilizzata per creare un utente, in particolare viene fatto l'hashing della password con
    l'algoritmo sha256 che produrrà un digest. Questo verrà conservato nel database.
    """

    data = request.json
    hashed_password = generate_password_hash(data["password"], method="sha256")

    uniqueID = str(uuid.uuid4()).replace("-", "")

    user = User(
        email=data["email"],
        password=hashed_password,
        nome=data["nome"],
        cognome=data["cognome"],
        data_nascita=data["data_nascita"],
        public_id=uniqueID
    )

    try:
        db.session.add(user)
        db.session.commit()
    except IntegrityError:
        return jsonify({"error": "L'email risulta già presente all'interno del nostro database."}), 400

    return createToken(user)


@auth.route("/logout", methods=["POST"])
@authenticated
def logout(current_user):
    token = request.headers.get('Authorization').replace("Bearer ", "")
    tokORM: Token = Token.query.filter_by(token=token).first()

    tokORM.revoked_date = datetime.datetime.now()

    db.session.commit()

    return {"ok": "token invalidato, logout eseguito con successo."}


@auth.route("/login", methods=["POST"])
@ValidateSchema(LoginSchema)
def login():
    data = request.json
    user = User.query.filter_by(email=data["email"]).first()

    if not user or not check_password_hash(user.password, data["password"]):
        return jsonify({"error": "Le credenziali inserite risultano sbagliate."}), 400

    return createToken(user)


def createToken(user):
    expire_date = datetime.datetime.now() + datetime.timedelta(hours=1)

    token = jwt.encode(
        payload={
            "sub": user.public_id,
            "exp": expire_date
        },
        key=environ.get("SECRET_KEY").encode(),
        algorithm="HS256"
    )

    tokenORM = Token(
        token=token,
        expire_date=expire_date,
        revoked_date=None,
        owner=user.user_id
    )

    db.session.add(tokenORM)
    db.session.commit()

    return jsonify({"token": token, "expire_date": expire_date.strftime("%d/%m/%Y %H:%M:%S")})


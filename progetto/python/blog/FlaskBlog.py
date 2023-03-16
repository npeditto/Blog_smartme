import os

from blog.utils.metaclasses.Singleton import Singleton

from flask import Flask
from flask_marshmallow import Marshmallow

from dotenv import load_dotenv

from blog.utils.RestAPI import RestAPI
from blog.models.Database import db

from blog.utils.Mocker import Mocker

from blog.resources.AuthResource import auth


class FlaskBlog(metaclass=Singleton):
    """
        Classe iniziale impiegata per la creazione degli oggetti necessari
        per l'inizializzazione delle RESTful API. In particolare è stato
        adoperato Flask per la sua semplicità e per le numerose librerie che
        hanno permesso di sviluppare una API RESTful in maniera semplice.
    """

    def __init__(self):
        self.app = Flask(__name__)
        self.api = RestAPI(self.app)

        self.config()

        self.db = db
        self.db.init_app(self.app)

        self.start_mocking()

        self.ma = Marshmallow(self.app)

    def config(self):
        # Caricamento variabili .env per la configurazione dell'oggetto flask.
        load_dotenv()

        self.app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get("DB_URI")
        self.app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = os.environ.get("SQL_TRACK_MOD")

        self.app.register_blueprint(auth)
        self.api.loadURIs()

    def get(self):
        return self.app

    def start_mocking(self):
        with self.app.app_context() as ctx:
            mocker = Mocker(100, 200)
            mocker.generate(ctx)


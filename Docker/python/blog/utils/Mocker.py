import datetime
import uuid

from blog.models.User import User
from blog.models.Post import Post
from blog.models.Database import db

from werkzeug.security import generate_password_hash
import faker
from random import choice, randint
from datetime import datetime, timedelta


class Mocker:
    def __init__(self, users, posts):
        self.usersQta = users
        self.postQta = posts

    def generate(self, context):
        password = generate_password_hash("password", method="sha256")

        fake = faker.Faker("IT_it")
        usersList = []

        emailsList = set()

        domains = ["yahoo.it", "yahoo.com", "live.it", "outlook.com", "libero.com", "gmail.it"]

        # Creazione email utente ed utenti del database.

        while len(emailsList) < self.usersQta:
            nome, cognome = fake.first_name(), fake.last_name()
            emailsList.add("%s.%s@%s" % (nome.lower(), cognome.lower(), choice(domains)))

        emailsList = list(emailsList)

        with context as _:
            db.drop_all()
            db.create_all()

            uniqueID = str(uuid.uuid4()).replace("-", "")
            user = User(
                nome="Emanuele",
                cognome="Pannuccio",
                email="pannuccio93@gmail.com",
                password=password,
                data_nascita=datetime.today() - timedelta(days=22 * 365),
                public_id=uniqueID
            )

            db.session.add(user)
            db.session.commit()

            for _ in range(self.usersQta):
                # La generazione di un UUID permette di impedire all'utente di vedere la logica di generazione
                uniqueID = str(uuid.uuid4()).replace("-", "")

                nome, cognome = fake.first_name(), fake.last_name()
                email = choice(emailsList)
                user = User(
                    nome=nome,
                    cognome=cognome,
                    email=email,
                    password=password,
                    data_nascita=datetime.today() - timedelta(days=randint(18, 50) * 365),
                    public_id=uniqueID
                )

                # Rimuovo la mail dalla lista al fine di assicurarmene l'univocità della sua esistenza.
                emailsList.remove(email)

                usersList.append(user)

            db.session.add_all(usersList)

            db.session.commit()

            for _ in range(self.postQta):
                post = Post(
                    contenuto=fake.text(),
                    autore=choice(usersList).user_id
                )

                db.session.add(post)
                db.session.commit()

from blog.models.Database import db

class User(db.Model):
    __tablename__ = "users"

    user_id = db.Column(db.BigInteger, primary_key=True)
    email = db.Column(db.String(255), unique=True, nullable=False)
    password = db.Column(db.String(255), nullable=False)

    nome = db.Column(db.String(255), nullable=False)
    cognome = db.Column(db.String(255), nullable=False)

    data_nascita = db.Column(db.Date, nullable=False)

    public_id = db.Column(db.String(255), nullable=False)

    posts = db.relationship("Post", backref="users")



    public_attribute = ("email", "nome", "cognome", "data_nascita", "_links")

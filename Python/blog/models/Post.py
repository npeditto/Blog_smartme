from blog.models.Database import db


class Post(db.Model):
    __tablename__ = "posts"

    post_id = db.Column(db.BigInteger, primary_key=True)
    contenuto = db.Column(db.Text, nullable=False)

    autore = db.Column(db.BigInteger, db.ForeignKey("users.user_id"), nullable=False)

    public_attribute = ("post_id", "contenuto", "_links")

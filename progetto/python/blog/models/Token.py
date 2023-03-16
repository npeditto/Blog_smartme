from blog.models.Database import db

class Token(db.Model):
    __tablename__ = "tokens"

    token_id = db.Column(db.BigInteger, primary_key=True)

    token = db.Column(db.String(255), nullable=False)

    expire_date = db.Column(db.DateTime, nullable=False)

    revoked_date = db.Column(db.DateTime, nullable=True)

    owner = db.Column(db.BigInteger, db.ForeignKey("users.user_id"), nullable=False)

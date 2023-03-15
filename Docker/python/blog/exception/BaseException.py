from flask_restful import HTTPException


class BaseException(HTTPException):
    def __init__(self, message="Errore generico."):
        super().__init__(message)
        self.message = message
        self.status = 500


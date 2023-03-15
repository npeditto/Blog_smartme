from blog.exception.BaseException import BaseException


class BadCredentialException(BaseException):
    def __init__(self, message="Credenziali errate"):
        super().__init__(message)
        self.status = 400

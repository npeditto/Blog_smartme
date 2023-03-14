from blog.exception.BaseException import BaseException


class TokenInvalidException(BaseException):
    def __init__(self):
        super().__init__("Token scaduto o non valido.")
        self.status = 400
from blog.exception.BaseException import BaseException

class ResourceNotFoundException(BaseException):
    def __init__(self, message="La risorsa non è stata trovata"):
        super().__init__(message)
        self.status = 404

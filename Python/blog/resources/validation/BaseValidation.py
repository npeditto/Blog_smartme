from marshmallow import Schema


class BaseValidationSchema(Schema):
    error_messages = {
        "required": "Il campo indicato non risulta essere presente.",
    }

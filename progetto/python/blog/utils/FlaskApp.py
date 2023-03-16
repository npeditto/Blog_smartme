from blog.FlaskBlog import FlaskBlog

flask = FlaskBlog()
app = flask.app


@app.errorhandler(404)
def handler_404(e):
    """
    Gestione dell'errore 404 Not Found, essendo in un ambito
    legato alle API si è preferito l'utilizzo di un JSON come risposta
    per i codici di errore più comuni.
    :param e:
    :return:
    """
    return {"error": "La pagina richiesta non è stata trovata."}

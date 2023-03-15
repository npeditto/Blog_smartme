FROM python:3.11-alpine

# Aggiorno pip
RUN pip install --upgrade pip

WORKDIR /usr/src/app

COPY requirements.txt .

RUN pip install -r requirements.txt

RUN cp /usr/share/zoneinfo/Europe/Rome /etc/localtime && echo "Europe/Rome" >  /etc/timezone 

CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0"]

FROM python:3

WORKDIR /usr/src/app

COPY requirements.txt ./

RUN pip install --no-cache-dir -r requirements.txt

COPY app.py .

COPY mongo.py .

EXPOSE 5000

RUN chmod 644 app.py

CMD ["python", "app.py"]

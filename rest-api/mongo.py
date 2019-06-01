import os
from pymongo import MongoClient

client = MongoClient("mongodb://db:27017")

db = client.reddit
submissions = db.submission
comments = db.comment

def read_from_mongo(subreddit, from_timestamp, to_timestamp):
    query = {
        "subreddit": subreddit,
        "creationTimestamp": {
            "$gt": int(from_timestamp),
            "$lt": int(to_timestamp)
        }
    }
    result = list()
    for x in comments.find(query):
        result.append(x)
    for x in submissions.find(query):
        result.append(x)
    return result

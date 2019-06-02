import pymongo

from pymongo import MongoClient

client = MongoClient("mongodb://db:27017")

db = client.reddit
submissions = db.submission
comments = db.comment


def read_from_mongo(subreddit, from_timestamp, to_timestamp, keyword):
    query = {
        '$and': [
            {'subreddit': subreddit},
            {
                '$and': [
                    {'creationTimestamp': {'$gt': int(from_timestamp)}},
                    {'creationTimestamp': {'$lt': int(to_timestamp)}}
                ]
            }
        ]
    }

    if keyword is not None:
        query['$and'].append({'$text': {'$search': "\"" + keyword + "\""}})

    result = list()
    for x in comments.find(query).sort('creationTimestamp', pymongo.DESCENDING):
        result.append(x)
    for x in submissions.find(query).sort('creationTimestamp', pymongo.DESCENDING):
        result.append(x)
    return result

# reddit-consumer
Consumes reddit submissions and comments for a list of subreddits, saves them to Mongo and exposes through a rest api. 

To run it you can use docker with this command: docker-compose up

For the data-retriever, you can configure the list of subreddits to be monitored by editing the application.yml file. The data-retriever will monitor the configured subreddits for the submissions and comments made and it will store them in MongoDB, in the "reddit" database.

With the rest-api you can query the submissions and comments stored in the MongoDB "reddit" database, by the subreddit name and the creation time by range. Example: curl -X GET http://127.0.0.1:5000/items?subreddit=Python&from=1558776675&to=1558789675 . 

There is an optional parameter named 'keyword' to make a text search in a submission's title and a comment's text
http://127.0.0.1:5000/items?subreddit=java&from=0&to=2558776675&keyword=extracting

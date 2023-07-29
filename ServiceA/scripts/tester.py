import requests
import threading

circuitBreakerUrl = 'http://localhost:7001/api/v1/upstream/product/cb/'
retryUrl = 'http://localhost:7001/api/v1/upstream/product/retry/'
timelimitter = 'http://localhost:7001/api/v1/upstream/product/tl/'
rateLimiter = 'http://localhost:7001/api/v1/upstream/product/rl/'
bulkHeadLimiter = 'http://localhost:7001/api/v1/upstream/product/bh/'
allAnnotationLimiter = 'http://localhost:7001/api/v1/upstream/product/all/'

def testCircuitBreaker():
     for x in range(50):
        url = circuitBreakerUrl + str(x)
        print(requests.get(url).content)


def testRetry():
     for x in range(50):
        url = retryUrl + str(x)
        print(requests.get(url).content)


def testTimeLimiter():
     for x in range(50):
        url = timelimitter + str(x)
        print(requests.get(url).content)


def testRateLimiter():
     for x in range(50):
        url = rateLimiter + str(x)
        print(requests.get(url).content)



def testBulkHeadLimiter():
    def callBulkHeadLimiter(x):
        url = bulkHeadLimiter + str(x)
        print(requests.get(url).content)

    for x in range(50):
        threading.Thread(target=callBulkHeadLimiter, args=(x,)).start()


def testAllLimiterWithMultiThread():
    def callBulkHeadLimiter(x):
        url = allAnnotationLimiter + str(x)
        print(requests.get(url).content)

    for x in range(50):
        threading.Thread(target=callBulkHeadLimiter, args=(x,)).start()



def testAllLimiterSingleThread():
    for x in range(50):
        url = allAnnotationLimiter + str(x)
        print(requests.get(url).content)



testCircuitBreaker()
testRetry()
testTimeLimiter()
testRateLimiter()
testBulkHeadLimiter()
testAllLimiterWithMultiThread()
testAllLimiterSingleThread()

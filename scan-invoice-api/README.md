# User stories for the API

In what follows we describe the user stories for the API.

# As a YNAB user, I want to import a NFC-e cupom to my YNAB budget

Using my mobile phone or through a web application, I send a NFC-e URL and expect the transaction to be added to a YNAB account.

**Information passed by the user:**

* NFC-e URL

**Information required for processing:**

* YNAB authorization token
* YNAB budget
* YNAB account

## Sequence diagram: request URL for processing
```text
title Request URL for processing

Mobile/WEB->+API: request URL
API->API: turl=InvoiceURLTransaction
API->+DB: tdb=query turl
DB-->-API: exists(turl)
alt exists(turl)
    API->API: t=tdb, etag=contents hash, cache=24 h
else not exists(turl)
    API->+Queue: queue turl
    Queue-->-API: queued
    API->API: t=turl, etag=url hash, cache=5 min
end

```

### Sequence diagram: request URL for processing, DB out of service error
```text
title Request URL for processing, DB out of service error

Mobile/WEB->+API: request URL
API->API: turl=InvoiceURLTransaction
API->DB: tdb=query turl
DB-->-API: error
API-->-Mobile/WEB: error 1: internal storage out of service (cache: no cache)
```

## Sequence diagram: poll URL processing
```text
title Poll URL for processing

Mobile/WEB->+API: poll URL
API->API: turl=InvoiceURLTransaction
API->+DB: tdb=query turl
DB-->-API: exists(turl)
alt exists(turl)
    API->API: t=tdb, etag=contents hash, cache=24 h
else not exists(turl)
    API->API: t=turl, etag=url hash, cache=3 s
end
API-->-Mobile/WEB: t, etag, cache
```

### Sequence diagram: poll URL processing, DB out of service error
```text
title Poll URL for processing, DB out of service error

Mobile/WEB->+API: poll URL
API->API: turl=InvoiceURLTransaction
API->+DB: tdb=query turl
DB-->-API: error
API-->-Mobile/WEB: error 1: internal storage out of service (cache: no cache)
```

## Sequence diagram: process URL
```text
title Process URL

Processor->+Queue: turl=get InvoiceURLTransaction
Queue-->-Processor: turl
Processor->+DB: save turl in processing
DB-->-Processor: safe
Processor->+Queue: remove turl from queue
Queue-->-Processor:
Processor->+InvoiceURLTransaction: save turl to db media
InvoiceURLTransaction->+URL: open stream to get contents
URL->+InputStream: create
InputStream-->-URL: ready
URL-->-InvoiceURLTransaction: stream ready
InvoiceURLTransaction->+InvoiceURLTransaction: create YNAB transaction
InvoiceURLTransaction->+InputStream: get contents
InputStream-->-InvoiceURLTransaction: xml=xml contents
InvoiceURLTransaction->InvoiceURLTransaction: json=create json
InvoiceURLTransaction->+DB: save url, xml, and json
DB-->-InvoiceURLTransaction: tdb safe
InvoiceURLTransaction-->-InvoiceURLTransaction: tdb
InvoiceURLTransaction-->-Processor: safe
```

### Sequence diagram: process URL, DB out of service error during queue removal
```text
title Process URL, DB out of service error during queue removal

Processor->+Queue: turl=get InvoiceURLTransaction
Queue-->-Processor: turl
Processor->+DB: save turl in processing
DB-->-Processor: error
```

### Sequence diagram: process URL, DB out of service error during URL processing 
```text
title Process URL, DB access error during URL processing

Processor->+Queue: turl=get InvoiceURLTransaction
Queue-->-Processor: turl
Processor->+DB: save turl in processing
DB-->-Processor: safe
Processor->+Queue: remove turl from queue
Queue-->-Processor:
Processor->+InvoiceURLTransaction: save turl to db media
InvoiceURLTransaction->+URL: open stream to get contents
URL->+InputStream: create
InputStream-->-URL: ready
URL-->-InvoiceURLTransaction: stream ready
InvoiceURLTransaction->+InvoiceURLTransaction: create YNAB transaction
InvoiceURLTransaction->+InputStream: get contents
InputStream-->-InvoiceURLTransaction: xml=xml contents
InvoiceURLTransaction->InvoiceURLTransaction: json=create json
InvoiceURLTransaction->+DB: save url, xml, and json
DB-->-InvoiceURLTransaction: error
InvoiceURLTransaction-->-InvoiceURLTransaction: error
InvoiceURLTransaction-->Processor: error
```
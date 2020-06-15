from kafka import KafkaProducer
import os
import sys
import json
import uuid
import argparse
from random import uniform, randint
from datetime import datetime, timedelta
from tzlocal import get_localzone

# Producer
producer = KafkaProducer(bootstrap_servers=['localhost:9092'], acks='all', retries=100, linger_ms=20, value_serializer=lambda m: json.dumps(m).encode('utf-8'))

parser = argparse.ArgumentParser(description='To generate records')
parser.add_argument('--records', help='Number of records to generate', required=True)

args = parser.parse_args()

total=int(args.records)
accounts_topic="accounts"
transactions_topic="transactions"
balances_assert_topic="balances_assert"

base_date = datetime.now(get_localzone())

for x in range(total):
    data = {}

    key = str(uuid.uuid4())

    data['identifier'] = key

    owned_by = {}
    owned_by['identifier'] = str(uuid.uuid4())
    owned_by['givenName'] = "Given" + str(x)

    data['ownedBy'] = owned_by

    # Accounts
    account_meta = producer.send(accounts_topic, key=key, value=data)
    print(account_meta.get(timeout=10).partition)

    # Balance for assertions
    balance = 0
    
    t_count = randint(2, 10)
    for txi in range(t_count):
        tx = {}

        tx['accountIdentifier'] = key
        tx['identifier'] = str(uuid.uuid4())

        base_date = base_date - timedelta(milliseconds=6000)
        tx['time'] = base_date.isoformat()

        if txi == 0: 
            tx['title'] = "Initial balance"
            tx['info'] = "deposit"
            tx['amount'] = format(uniform(1000, 65000), '.4f')
        else:
            tx['title'] = "Transaction " + tx['identifier']
            tx['info'] = "Some transaction info"
            tx['amount'] = format(uniform(-325, 650), '.4f')
        
        balance = balance + float(tx['amount'])

        # Transactions
        tx_meta = producer.send(transactions_topic, key=key, value=tx)
        print(tx_meta.get(timeout=10).partition)

    # Balances for assertions
    bl = {}
    bl['balance'] = format(balance, '.4f')
    balance_meta = producer.send(balances_assert_topic, key=key, value=bl)
    print(balance_meta.get(timeout=10).partition)

producer.flush()
producer.close()
import os
import sys
import json
import uuid
import argparse
from random import uniform, randint
from datetime import datetime, timezone, timedelta

parser = argparse.ArgumentParser(description='To generate records')
parser.add_argument('--records', help='Number of records to generate', required=True)

args = parser.parse_args()

total=int(args.records)
accounts_file="accounts.txt"
transactions_file="transactions.txt"
balances_file="balances.txt"

base_date = datetime.now(timezone.utc)

if os.path.exists(accounts_file):
    os.remove(accounts_file)

if os.path.exists(transactions_file):
    os.remove(transactions_file)

if os.path.exists(balances_file):
    os.remove(balances_file)

a = open(accounts_file, "w")
t = open(transactions_file, "w")
b = open(balances_file, "w")

for x in range(total):
    data = {}

    key = str(uuid.uuid4())

    data['identifier'] = key

    owned_by = {}
    owned_by['identifier'] = str(uuid.uuid4())
    owned_by['givenName'] = "Given" + str(x)

    data['ownedBy'] = owned_by

    account = json.dumps(data)

    # Accounts
    a.write(key + "|" + account)
    a.write("\n")

    balance = 0
    # Transactions per account
    t_count = randint(2, 6)
    for txi in range(t_count):
        tx = {}

        tx['accountIdentifier'] = key
        tx['identifier'] = str(uuid.uuid4())

        base_date = base_date - timedelta(milliseconds=6000)
        tx['time'] = base_date.astimezone().isoformat()

        if txi == 0: 
            tx['title'] = "Initial balance"
            tx['info'] = "deposit"
            tx['amount'] = 2000.00
        else:
            tx['title'] = "Transaction " + tx['identifier']
            tx['info'] = "Some transaction info"
            tx['amount'] = format(uniform(-325, 650), '.4f')
        
        balance = balance + float(tx['amount'])

        transaction = json.dumps(tx)

        # Transactions
        t.write(key + "|" + transaction)
        t.write("\n")

    # Balances for assertions
    bl = {}
    bl['balance'] = format(balance, '.4f')
    b.write(key + "|" + json.dumps(bl))
    b.write("\n")

a.close()
t.close()

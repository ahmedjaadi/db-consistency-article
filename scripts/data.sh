#!/usr/bin/env bash

DB_HOST="postgres"
DB_USER="user"
DB_NAME="example"
DB_PORT="5432"

BATCH_SIZE=1000
TOTAL_ROWS=5000
TABLE_NAME="sale"

psql -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" <<EOF
DROP TABLE IF EXISTS $TABLE_NAME;

CREATE TABLE $TABLE_NAME (
    id SERIAL PRIMARY KEY,
    status VARCHAR(20),
    sold DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
EOF

# We'll build a single huge INSERT statement in a variable (or in a temp file)
INSERT_SQL="INSERT INTO $TABLE_NAME (status, sold) VALUES"

count=0
for i in $(seq 1 $TOTAL_ROWS); do
  # Randomly choose the status
  STATUS="NOT_INITIALIZED"

  # Generate a random sold amount
  RAND_INT=$((RANDOM % 1000 + 1))
  RAND_DECIMAL=$(printf "%.2f" "$(awk -v num="$RAND_INT" 'BEGIN{srand(); print num + rand()}')")

  # Append to our INSERT statement
  INSERT_SQL="${INSERT_SQL} ('$STATUS', $RAND_DECIMAL),"

  # Every BATCH_SIZE rows, or if we're at the end, insert them and reset
  if (( i % BATCH_SIZE == 0 || i == TOTAL_ROWS )); then
    # Remove the trailing comma
    INSERT_SQL="${INSERT_SQL%,};"

    # Execute the batched INSERT
    psql -U "$DB_USER" -d "$DB_NAME" -c "$INSERT_SQL" >/dev/null

    # Prepare for next batch
    INSERT_SQL="INSERT INTO $TABLE_NAME (status, sold) VALUES"
  fi

  # Optional progress
  if (( i % 5000 == 0 )); then
    echo "$i rows processed..."
  fi
done
echo "Done!"

#!/bin/bash
# Script to fix the database by adding the isadmin column

# PostgreSQL connection details
PGHOST="localhost"
PGPORT="5432"
PGDATABASE="postgres"
PGUSER="postgres"
PGPASSWORD="viaviavia"

# SQL to add the isadmin column if it doesn't exist
echo "Executing SQL to add isadmin column to user table..."

psql -h $PGHOST -p $PGPORT -d $PGDATABASE -U $PGUSER << EOF
-- Check if the column exists and add it if it doesn't
DO \$\$
BEGIN
    -- Check if the column exists
    IF NOT EXISTS (
        SELECT FROM information_schema.columns 
        WHERE table_schema = 'summerhouse_rental_system'
        AND table_name = 'user'
        AND column_name = 'isadmin'
    ) THEN
        -- Add the column if it doesn't exist
        ALTER TABLE summerhouse_rental_system."user" ADD COLUMN isadmin BOOLEAN NOT NULL DEFAULT false;
        RAISE NOTICE 'Column isadmin added to user table';
    ELSE
        RAISE NOTICE 'Column isadmin already exists in user table';
    END IF;
END \$\$;

-- Verify the column was added
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_schema = 'summerhouse_rental_system' 
AND table_name = 'user';
EOF

echo "Database update complete!"

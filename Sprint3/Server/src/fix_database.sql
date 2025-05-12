-- Script to fix the database by adding the isadmin column to the user table
-- Run this in PostgreSQL using: psql -f fix_database.sql -h localhost -p 5432 -d postgres -U postgres

-- Switch to the correct schema
SET search_path TO summerhouse_rental_system;

-- Add the column if it doesn't exist
DO $$
BEGIN
    -- Check if the column exists
    IF NOT EXISTS (
        SELECT FROM information_schema.columns 
        WHERE table_schema = 'summerhouse_rental_system'
        AND table_name = 'user'
        AND column_name = 'isadmin'
    ) THEN
        -- Add the column if it doesn't exist
        ALTER TABLE "user" ADD COLUMN isadmin BOOLEAN NOT NULL DEFAULT false;
        RAISE NOTICE 'Column isadmin added to user table';
    ELSE
        RAISE NOTICE 'Column isadmin already exists in user table';
    END IF;
END $$;

-- Verify the column was added
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_schema = 'summerhouse_rental_system' 
AND table_name = 'user';

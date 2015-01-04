ALTER TABLE C01_RECIPE ADD COLUMN C01_SEARCHABLE_TEXT TEXT NULL AFTER C01_RECIPE_CONTENT;

--*****************************************************************************************
-- now must run app initializer that fills out the column before running the next statement
--*****************************************************************************************

ALTER TABLE C01_RECIPE MODIFY C01_SEARCHABLE_TEXT TEXT NOT NULL;
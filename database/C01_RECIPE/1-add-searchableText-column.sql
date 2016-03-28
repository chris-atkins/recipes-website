ALTER TABLE C01_RECIPE ADD COLUMN C01_SEARCHABLE_TEXT TEXT NULL AFTER C01_RECIPE_CONTENT;

UPDATE C01_RECIPE SET C01_LAST_UPDATED_ON = CURRENT_TIMESTAMP() WHERE C01_RECIPE_ID < 100 AND C01_LAST_UPDATED_ON IS NULL;

/*****************************************************************************************
 now must run app initializer that fills out the column before running the next statement
*****************************************************************************************/

ALTER TABLE C01_RECIPE MODIFY C01_SEARCHABLE_TEXT TEXT NOT NULL;

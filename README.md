# Bricks box

## Data Scraping

### Start
```./brickset-data-scraping.py <configuration-file-path>```

### Configuration: brickset-scrapping.yaml

field | default value | required | description
--- | --- | --- | ---
logging.file | - | true | Absolute path to logging file
logging.path | - | true | Absolute path to logging directory
logging.format | '%(asctime)s - %(levelname)s - %(message)s' | true | Single log format
data.path | - | true | Absolute path to data store directory
data.endpoints_file | - | true | Absolute path to endpoints file
data.sets_by_theme_file | - | true | Absolute path to bricks set file
data.delta_file | - | true | Absolute path to single run delta file
api.url | - | true | Url to api

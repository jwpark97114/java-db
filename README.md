#Java DB



# TABLE BASE STORAGE

## What do we need

1. Current Saving System won't work as this is not scalable
2. I need to find a way to not only store a line of data but also Table information itself
      - Some kind of metadata of this Database must be saved and checked for saving and loading
3. With the split in Metadata and actual data, I also have to find a way to separate tables
4. Parsing will have to change



## Things to change vs leave

### We can keep
1. serialization method
2. inputInterface


### EVERYTHING ELSE MUST GO



## What classes or methods do I need?

### Table class
### Metadata class
### Row class
### DB class
### QueryEngine class
### Possibly new parser class?



1. Table has multiple rows, row must be made with the constraints from that table's metadata such as what columns it has and so on
2. Should Row understand what values it holds? 
2. DB has multiple Tables
3. Operation on Table must be reflected to file all the time - no more in memory operations
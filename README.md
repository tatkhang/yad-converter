# YadConverter
Yet another data converter (YadConverter) is a simple Java CLI tool that help convert data in CSV format to Parquet and via versa.

## Inspiration

## Setup
- Install Java 11
- Install Maven
- Windows users must configure the environment variable %HADOOP_HOME% follow https://github.com/cdarlint/winutils

## Usage
- Navigate to project root
- Compile project with command `mvn clean package`
- Run application with command `java -jar target/yad-converter.jar -i <input file path> -o <output file path>`

## Room for improvement
- Allow file configuration:
  + CSV delimiter
  + Parquet row group size
- Overwrite output file option
- Show working progress
- Integrate logging
- Performance consideration for large file
  + Divide into smaller chunks
  + Consider using org.apache.spark which has already processed by dataframe
- Support other types of conversion
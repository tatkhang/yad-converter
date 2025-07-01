# YadConverter
Yet another data converter (YadConverter) is a simple Java CLI tool that help convert data in CSV format to Parquet and via versa.

## Inspiration

## Setup
- Install Java 11
- Install Maven
- Windows users must configure the environment variable %HADOOP_HOME% follow https://github.com/cdarlint/winutils

## Usage
- Navigate to project root
- Compile project with command `mvn compile`
- Run application with command `java -jar target/yad-converter-0.0.1-SNAPSHOT.jar -i <input file path> -o <output file path>`

## Room for improvement
- Allow file configuration:
  + CSV delimiter
  + Parquet row group size
- Integrate logging
- Show working progress
- Performance consideration for large file
- Overwrite output file option
- Support other type of conversion
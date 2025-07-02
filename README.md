# YadConverter
Yet another data converter (YadConverter) is a simple Java CLI tool for converting data between CSV and Parquet formats.

---

## Table of Contents

- [Setup](#setup)
- [Usage](#usage)
- [Testing & Code Coverage](#testing--code-coverage)
- [Design](#design)
- [Room for Improvement](#room-for-improvement)

---

## Setup

1. **Install Java 11**
2. **Install Maven**
3. **Windows users:**  
   Set the `%HADOOP_HOME%` environment variable.  
   See [cdarlint/winutils](https://github.com/cdarlint/winutils) for details.

## Usage

1. Navigate to the project root.
2. Compile the project:
   ```sh
   mvn package
   ```
3. Run the application:
   ```sh
   java -jar target/yad-converter.jar -i <input file path> -o <output file path>
   ```

---

## Testing & Code Coverage

- Run tests:
  ```sh
  mvn test
  ```
- After running tests, a `jacoco.exec` file will be generated in the `target` folder.
- To generate an HTML coverage report:
  1. [Download JaCoCo](https://www.jacoco.org/jacoco/) and extract it (e.g., to a `jacoco` directory).
  2. Locate `jacococli.jar` in `jacoco/lib/`.
  3. Run the following command from the JaCoCo directory:
     ```sh
     java -jar lib/jacococli.jar report path/to/jacoco.exec --classfiles path/to/your.jar --html path/for/report
     ```
  4. Open `index.html` in the generated report directory to view the coverage.

  > Reference: [StackOverflow answer](https://stackoverflow.com/a/55398682)

---

## Design

---

## Room for Improvement

- Allow file configuration:
  - CSV delimiter
  - Parquet row group size
- Option to overwrite output files
- Show progress during conversion
- Integrate logging
- Performance improvements for large files:
  - Chunk processing
  - Consider using [Apache Spark](https://spark.apache.org/) for DataFrame-based processing
- Support additional conversion types

---
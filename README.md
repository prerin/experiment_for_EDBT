## LSiX code for evaluation experiment

### Configuration
・src/main/java/count_experiment: Sum experiment code for count-based window \
・src/main/java/count_experiment_max: Max experiment code for count-based window \
・src/main/java/count_experiment_std: Std experiment code for count-based window \
・src/main/java/time_experiment: Sum experiment code for time-based window \
・src/main/java/time_experiment_max: Max experiment code for time-based window \
・src/main/java/time_experiment_std: Std experiment code for time-based window


### Author's execution environment
IDE: IntelliJ IDEA 2023.1 \
SDK: BellSoft Liberica version 1.8 \
(Results may vary depending on the environment.)

### Execution method
First, download the zip file from the following [url](https://drive.google.com/file/d/1cRDOrkIsoSmdqqN6RtYt3XavXCP0Bp7l/view?usp=sharing).
Unzip that file and place the two txt files in a folder directly below.

Then, by running main.java in each package, the time for each method can be measured.\
Commenting out the figure below in main.java allows experiment with varying numbers of queries.
<p>
  <img src="https://github.com/prerin/experiment_for_EDBT/blob/master/img/query_num_1.png" height="256px">
  <img src="https://github.com/prerin/experiment_for_EDBT/blob/master/img/query_num_2.png" height="256px">
</p>

Commenting out the figure below allows experiment with varying window size.
<p>
  <img src="https://github.com/prerin/experiment_for_EDBT/blob/master/img/window_size_1.png" height="256px">
  <img src="https://github.com/prerin/experiment_for_EDBT/blob/master/img/window_size_2.png" height="256px">
</p>

# Experiment
Calibre Automatic Delivery of FX Data

I have used Spring Tool Suite 4 to develop this project.
here is the link to download this tool
https://spring.io/tools


Please make these changes to run this project.

1-- Open the application.properties file and change the user name (Whatever your gmail address is)
Properties to change: spring.mail.username and spring.mail.password

2-- In the same file, please also change the control.emailTo property (Whoever you wants to send the data)

3-- In the same file, also change the control.token to your own provided token by the forex rate provider (The current one written is the demo one and only works for EUR.FOREX currency).

4-- The log file logFile.log is saved in the root directory and the data file is saved inside src folder.

5-- For testing the application,  you can remove the comments from @Scheduled(fixedRate = 1000) line before the performExperiment() method and put comments on  @Scheduled(cron = "0 0 16 * * ?")
@Scheduled(cron = "0 0 12 * * ?")
@Scheduled(cron = "0 0 08 * * ?")
as they only send email at 8am, 12pm, and 4pm.


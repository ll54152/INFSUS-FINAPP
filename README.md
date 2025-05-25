<h1>Informacijski sustavi - DZ3 </h1>

<h2>Requirements:</h2>
<ul>
<li>Node.js (https://nodejs.org/)</li>
<li>npm (comes with Node.js)</li>
<li>Apache Maven</li>
</ul>


<h2>Installation procedure</h2>
<h3>1. Acquire repository </h3>

Download repository by clicking on green button named "Code" and then clicking on "Download ZIP" or use this link: <br>
https://github.com/ll54152/INFSUS-FINAPP/archive/refs/heads/master.zip

Or alternatively, clone repository using: <br>
```sh
git clone https://github.com/ll54152/INFSUS-FINAPP.git
```

<h3>2. Start backend </h3>

Open command prompt in backend folder and execute the following command:
```sh
mvn spring-boot:run
```

<h3>3. Install frontend modules</h3>

Open command prompt in frontend folder and execute the following command:
```sh
npm install
```
<h3>4. Start application</h3>

After installation, start the application by executing the following command in command prompt:
```sh
npm run dev
```
<br>
<h2>Note:</h2>
<p>Frontend is running on port 5173 and backend is running on port 8080. If you want to change the ports, you can do so in the <code>vite.config.js</code> file for frontend and in the <code>application.properties</code> file for backend.</p>
<p>Database is hosted online but if you want to change to local test, you need to change <code>application.properties</code> for local connectin. Also, you can use SQL scripts for creating and inserting data.</p>


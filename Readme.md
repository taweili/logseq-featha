- Create logseq plugin project in Clojurescript
	- Use `shadow-cljs`
	  ```shell
	  $ npx create-cljs-project logseq-featha
	  ```
	- Run REPL
	  ```shell
	  $ npx shadow-cljs node-repl
	  # or
	  $ npx shadow-cljs browser-repl
	  ```
	- Test the `browser-repl`
	  ```clojurescript
	  $ npx shadow-cljs browser-repl
	  cljs.user => (js/console.log "Hello Console")
	  nil
	  cljs.user => (js/alert "Hello Alert")
	  nil
	  ```
	- Create the app in `src/main/featha`, create `app.cljs` with the skeleton codes
		```clojurescript
		(ns featha.app)
		(defn init []
		(println "Hello World"))
		```
	- Create the `index.html` inside the `public` directory with this template
		```html
		<!doctype html>
		<html>
		<head>
			<meta charset="utf-8" />
			<title>acme frontend</title>
		</head>
		<body>
			<div id="root"></div>
			<script src="js/main.js"></script>
		</body>
		</html>
		```
	- Edit the `shadow-cljs.edn` to include the app build
		```clojurescript
		;; shadow-cljs configuration
		{:source-paths
		["src/dev" "src/main" "src/test"]
		
		:dependencies
		[]
		
		:builds
		{:app
		{:target :browser
			:modules {:main {:init-fn featha.app/init}}}}}
		```
	- Run `shadow-cljs watch app`
		```shell
		$ shadow-cljs watch app
		shadow-cljs - config: C:\Users\david\Work\logseq-featha\shadow-cljs.edn
		shadow-cljs - nREPL server started on port 64540
		shadow-cljs - watching build :app
		```
	- Set up VSCode
		- Use [Calva: Clojure & ClojureScript Interactive Programming - Visual Studio Marketplace](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva)
		- Set up `djblue/portal` to work better with Calva. Configure in `shadow-cljs.edn`
		```clojurescript
		;; shadow-cljs configuration
		{:source-paths
		["src/dev" "src/main" "src/test"]
		
		:dependencies
		[
		[djblue/portal "0.51.0"]
		]
		:nrepl {:port 8702
				:middleware [portal.nrepl/wrap-portal]}
		
		:builds
		{:app
		{:target :browser
			:modules {:main {:init-fn featha.app/init}}}}}
		```
	- Set up Logseq App
        1. Enable developer mode in Logseq
		2. Click "Load unpacked plugin" and open the root folder of this project which contains the `package.json` and `dist` folder. Logseq plugin system requires entry `package.json` even in dev mode
		3. To open Logseq console for debugging, use Chrome's default hotkey. E.g. `Option Command + i` on MacOS.
		- For more information, see [Application Debugging | Electron](https://www.electronjs.org/docs/latest/tutorial/application-debugging)
	- Set up logseq plugin main entry point in `package.json` with `main`. Add `@logseq/lib` and `promesa` to dependencies.

		```json
		{
		"name": "logseq-featha",
		"version": "0.0.1",
		"main": "public/index.html",
		"dependencies": {
			"@logseq/libs": "0.0.15",
			"promesa": "0.1.1"
		},
		"devDependencies": {
			"shadow-cljs": "2.27.1"
		},
		"logseq": {
			"id": "_2w6p59whp",
			"title": "ClojureScript Plugin Skeleton"
		}
		}
		```
	- Run `npm install` to get all packages installed
	- Run `shadow-cljs watch app` and get start hacking logseq with ClojureScript
	- This `app.cljs` is the minimum skeleton codes to get through the logseq set up
	    
	  ```clojurescript
	  (ns featha.app
	    (:require
	     ["@logseq/libs"]
	     [promesa.core :as p]))
	  
	  (defn main []
	    (js/console.log "Hello from featha main")
	    (js/logseq.App.showMsg "❤️  Message from featha main"))
	  
	  (defn init []
	    (-> (p/promise (js/logseq.ready))
	        (p/then main)
	        (p/catch #(js/console.error))))
	  ```

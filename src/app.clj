(ns app
  (:require 
    [ring.adapter.jetty :refer [run-jetty]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.json :refer [wrap-json-response]]
    [compojure.handler :refer [api]]
    [ewallet-api.core :refer [app]])
  (:gen-class))

(defn run-dev-server
  "Starts application"
  [& args]
  (run-jetty (-> app  
    (wrap-reload)) {:port 3000}))
 

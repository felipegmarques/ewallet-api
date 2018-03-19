(ns app
  (:require 
    [ring.adapter.jetty :refer [run-jetty]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ewallet-api.core :refer [app]])
  (:gen-class))

(defn run-dev-server
  "Starts application"
  [& args]
  (run-jetty (-> app  
    (wrap-reload)) {:port 3000}))
 

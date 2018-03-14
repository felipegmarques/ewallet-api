(ns ewallet-api.core
  (:import java.text.SimpleDateFormat)
  (:require 
    [ring.adapter.jetty :refer :all]
    [ring.util.response :refer [response]]
    [ring.middleware.json :refer [wrap-json-response]]
    [compojure.core :refer :all]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [ewallet-api.api :refer :all])
  (:gen-class))

(def date-parser (SimpleDateFormat. "yyyy/MM/dd"))
(defn parse-date [date] (.parse date-parser date))

(defroutes app
  (GET "/entries" 
    [start-date end-date] 
    (get-entries-by-period (parse-date start-date) (parse-date end-date)))
  (GET "/summary" 
    [start-date end-date] 
    (response (get-summary-by-period (parse-date start-date) (parse-date end-date))))
  (POST "/save-entry"
    [value date description]
    
    (let [entry { :value value :date (parse-date date) :description description}]
      (save-entry entry)
      (response {:message "Success"})))
  (route/not-found "<h1>Page not found</h1>"))

(defn -main
  "Starts application"
  [& args]
  (run-jetty (-> app (handler/site) (wrap-json-response)) {:port 3000}))
  

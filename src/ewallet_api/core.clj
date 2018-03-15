(ns ewallet-api.core
  (:require 
    [ring.adapter.jetty :refer :all]
    [ring.util.response :refer [response]]
    [ring.middleware.json :refer [wrap-json-response]]
    [compojure.core :refer :all]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [clj-time.core :as time]
    [clj-time.format :as f]
    [ewallet-api.api :refer :all])
  (:gen-class))

(def date-format "yyyy/MM/dd")
(def date-formater (f/formatter date-format))
(defn parse-date [date] (f/parse date-formater date))
(defn unparse-date [date] (f/unparse date-formater date))

(defn as-date
  "Parse a string into date, or throw exception"
  [date]
  (try
    (parse-date date)
    (catch IllegalArgumentException e 
      (throw (ex-info "Invalid date" {:value date :expected-format date-format})))))

(defn as-number
  "Parse a number or throw exception"
  [x]
  (try 
    (Double/parseDouble x)
    (catch NumberFormatException e
      (throw (ex-info "Invalid number" {:value x })))))

(defn not-null
  "Validate if values is null"
  [name]
  (fn [x]
    (if (or (nil? x) (= "" x)) 
      (throw 
        (ex-info 
          (str "Argument '" name "' cannot be empty") 
          {:value x}))
      x)))

(defn wrap-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch clojure.lang.ExceptionInfo e
        {:status 400 :body (.getMessage e)}))))

(defroutes app
  (GET "/entries" 
    [start-date :<< as-date end-date :<< as-date] 
    (map 
      (fn [entry] (update entry :date unparse-date))
      (get-entries-by-period start-date end-date)))

  (GET "/summary" 
    [start-date :<< as-date end-date :<< as-date] 
    (response (get-summary-by-period start-date end-date)))

  (POST "/save-entry"
    [value :<< (comp as-number (not-null "value")) 
      date :<< as-date 
      description :<< (not-null "description")]
    (let [entry { :value value :date date :description description}]
      (save-entry entry)
      (response {:message "Success"})))
  (route/not-found "<h1>Page not found</h1>"))

(defn -main
  "Starts application"
  [& args]
  (run-jetty (-> app  
    (handler/site) 
    (wrap-exception) 
    (wrap-json-response)) {:port 3000}))
   
  

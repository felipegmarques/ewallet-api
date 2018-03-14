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

(def date-formater (f/formatter "yyyy/MM/dd"))
(defn parse-date [date] (f/parse date-formater date))
(defn unparse-date [date] (f/unparse date-formater date))

(defmacro validate-date
  "Macro that executes body with dates parsed or throw execption"
  [[date & other-dates] & expressions]
  (let [body (cons 'do expressions)]
    `(try (parse-date ~date)
      ~(if (empty? other-dates)
        body
        `(validate-date ~other-dates ~body))
      (catch IllegalArgumentException ~(gensym 'e) "Invalid date")))
)

(defroutes app
  (GET "/entries" 
    [start-date end-date] 
    (validate-date [start-date end-date]
      (map (fn [entry] (update entry :date unparse-date))
        (get-entries-by-period (parse-date start-date) (parse-date end-date)))
      ))
  (GET "/summary" 
    [start-date end-date] 
    (validate-date [start-date end-date]
      (response (get-summary-by-period (parse-date start-date) (parse-date end-date)))))
  (POST "/save-entry"
    [value date description]
    (validate-date [date]
      (let [entry { :value value :date (parse-date date) :description description}]
        (save-entry entry)
        (response {:message "Success"}))))
  (route/not-found "<h1>Page not found</h1>"))

(defn -main
  "Starts application"
  [& args]
  (run-jetty (-> app (handler/site) (wrap-json-response)) {:port 3000}))
  

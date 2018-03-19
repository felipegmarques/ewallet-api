(ns ewallet-api.routes
  (:require 
    [ring.util.response :refer [response]]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ewallet-api.common.validators :refer [as-date as-number not-null unparse-date]]
    [ewallet-api.api.api :refer :all])
  (:gen-class))

(defroutes app-routes
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

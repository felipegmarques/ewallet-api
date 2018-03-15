(ns ewallet-api.validators
  (:require 
    [clj-time.format :as f])
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

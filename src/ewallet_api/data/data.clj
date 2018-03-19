(ns ewallet-api.data.data
    (:require [clojure.string :refer [join split-lines split]]
              [clj-time.core :as time]
              [clj-time.format :as f]))

(def date-formater (f/formatter "yyyy/MM/dd"))

(defn parse-raw-entries
    "Parse csv string to entries"
    [raw-entries]
    (let [raw-entries (rest (split-lines raw-entries))]
        (map
            (fn [raw-entry]
                (let [[value date description] (split raw-entry #";")]
                    {:value (read-string value) :date (f/parse date-formater date) :description description})
            )
            raw-entries)
    )
)

(defn entries-to-csv
    "Transform Entries in CSV"
    [entries]
    (let [raw-entries (map 
        (fn [{:keys [value description date]}]
            (join ";" [value (f/unparse date-formater date) description])) 
        entries)]
        (str "value;date;description\n" (join "\n" raw-entries))))
(ns ewallet-api.data
    (:import java.text.SimpleDateFormat)
    (:require [clojure.string :refer [join split-lines split]]))

(def date-formater (SimpleDateFormat. "yyyy/MM/dd"))

(defn parse-raw-entries
    "Parse csv string to entries"
    [raw-entries]
    (let [raw-entries (rest (split-lines raw-entries))]
        (map
            (fn [raw-entry]
                (let [[value date description] (split raw-entry #";")]
                    {:value (read-string value) :date (.parse date-formater date) :description description})
            )
            raw-entries)
    )
)

(defn entries-to-csv
    "Transform Entries in CSV"
    [entries]
    (let [raw-entries (map 
        (fn [{:keys [value description date]}]
            (join ";" [value (.format date-formater date) description])) 
        entries)]
        (str "value;date;description\n" (join "\n" raw-entries))))
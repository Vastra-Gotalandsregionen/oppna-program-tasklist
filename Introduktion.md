#labels Featured

# Introduktion 

Med Java-portleten Mina uppgifter kan du se vad du har på din att-göra-lista samt hantera dessa (lägga till nya, redigera och ta bort befintliga).

För varje uppgift kan du ange en beskrivning, ett klart-datum och prioritet.

# Teknik 

Portleten är utformad enligt JSR-168/286 med hjälp av Spring Portlet MVC. AJAX används för att ge liv åt det grafiska gränssnittet och därmed krävs att webbläsaren har JavaScript aktiverat. Webbramverket YUI används för det grafiska gränssnittet.

För att lagra uppgifterna används en relationsdatabas. Denna är inte inbäddad i applikationen utan förutsätts finnas tillgänglig i nätverket. Databasanslutningsuppgifterna anges i filen [datasource.properties](http://code.google.com/p/oppna-program-tasklist/source/browse/trunk/core-bc/modules/portlet/src/main/resources/se/vgregion/portal/tasklist/datasource.properties) samt i security.properties. För att se exempel på hur security.properties ska se ut kan med fördel [http://code.google.com/p/oppna-program-tasklist/source/browse/trunk/core-bc/modules/portlet/src/main/resources/se/vgregion/portal/tasklist/security.properties.template security.properties.template] studeras.

## Databas 

Databastabellen som används heter _vgr_task_ och skapas genom att man kör [sql-skriptet](http://code.google.com/p/oppna-program-tasklist/source/browse/trunk/core-bc/sql/generate_task_table.sql).

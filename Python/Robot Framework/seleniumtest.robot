[Documentation]    Testing Robot Framework and SeleniumLibrary made for web testing. In this test I am using website SauceDemo.com which is made for practising automation.
[Documentation]    Here the login data for the test cases is read from an excel file called "testexcel". 

*** Settings ***
Library    SeleniumLibrary
Library    ExcelLibrary

*** Keywords ***
ReadExcelFile
    [Arguments]    ${pathname}    ${filename}    ${rownumber}    ${columnnumber}
    Open Excel Document    ${pathname}    1
    Get Sheet              ${filename}
    ${data}                Read Excel Cell    ${rownumber}    ${columnnumber}
    [Return]                ${data}
    Close Current Excel Document

ButtonClicks
    Click Button       add-to-cart-sauce-labs-backpack
    Click Button       add-to-cart-sauce-labs-bike-light
    Click Button       add-to-cart-sauce-labs-fleece-jacket
    Click Button       add-to-cart-sauce-labs-onesie
    Click Button       add-to-cart-sauce-labs-bolt-t-shirt
    Click Button       add-to-cart-test.allthethings()-t-shirt-(red)
    Click Element      shopping_cart_container    
    Click Button       checkout

InputInfo
    Input Text         first-name    test123
    Input Text         last-name     test123
    Input Text         postalCode    test123

FinishOrder
    Click Button       continue
    Click Button       finish
    Click Button       back-to-products
*** Variables ***
${browser}    chrome
${url}        https://www.saucedemo.com/
${path}       ${CURDIR}${/}..\\robotframeworktesting\\testexcel.xlsx 

*** Test Cases ***
ExcelTestCase1
    Open Browser       ${url}           ${browser}
    ${username}        ReadExcelFile    ${path}    Taul1    2    1
    ${password}        ReadExcelFile    ${path}    Taul1    2    2
    Input Text         user-name        ${username}
    Input Password     password         ${password}
    Click Button       login-button
    ButtonClicks
    InputInfo
    FinishOrder
    Close Browser

ExcelTestCase2
    Open Browser       ${url}           ${browser}
    ${username}        ReadExcelFile    ${path}    Taul1    3    1
    ${password}        ReadExcelFile    ${path}    Taul1    3    2
    Input Text         user-name        ${username}
    Input Password     password         ${password}
    Click Button       login-button
    ButtonClicks
    InputInfo
    FinishOrder
    Close Browser

ExcelTestCase3
    Open Browser       ${url}           ${browser}
    ${username}        ReadExcelFile    ${path}    Taul1    4    1
    ${password}        ReadExcelFile    ${path}    Taul1    4    2
    Input Text         user-name        ${username}
    Input Password     password         ${password}
    Click Button       login-button
    ButtonClicks
    InputInfo
    FinishOrder
    Close Browser

ExcelTestCase4
    Open Browser       ${url}           ${browser}
    ${username}        ReadExcelFile    ${path}    Taul1    5    1
    ${password}        ReadExcelFile    ${path}    Taul1    5    2
    Input Text         user-name        ${username}
    Input Password     password         ${password}
    Click Button       login-button
    ButtonClicks
    InputInfo
    FinishOrder
    Close Browser
    
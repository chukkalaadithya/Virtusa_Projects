from expense_manager import (
    add_expense,
    get_all_expenses,
    delete_expense,
    edit_expense
)
from analytics import category_summary, show_advanced_chart, monthly_summary
from config import VALID_CATEGORIES
from utils import get_valid_date, get_valid_amount, handle_category


def show_menu():
    print("\n====== Smart Expense Tracker ======")
    print("1. Add Transaction")
    print("2. View Transactions")
    print("3. Edit Transaction")
    print("4. Delete Transaction")
    print("5. Monthly Summary")
    print("6. View Insights (Graph)")
    print("7. Exit")


def display_expenses(data):
    if not data:
        print("\nNo transactions found.")
        return

    print("\n--- Transaction List ---")
    print("No | Date       | Category   | Amount   | Description")
    print("-" * 70)

    for i, e in enumerate(data, 1):
        print(f"{i:<3}| {e['date']:<10} | {e['category']:<10} | ₹{e['amount']:.2f} | {e['description']}")


while True:
    show_menu()
    choice = input("\nSelect an option: ").strip()

    if choice == "1":
        try:
            print("\n--- Add Transaction ---")

            date = get_valid_date()
            category = handle_category(VALID_CATEGORIES)
            amount = get_valid_amount()
            description = input("Enter description: ")

            confirm = input("Save this transaction? (y/n): ").lower()
            if confirm == "y":
                add_expense(date, category, amount, description)
                print(" Transaction added successfully.")
            else:
                print(" Transaction cancelled.")

        except Exception as e:
            print("↩ Operation cancelled:", e)

    elif choice == "2":
        display_expenses(get_all_expenses())

    elif choice == "3":
        data = get_all_expenses()
        display_expenses(data)

        try:
            idx_input = input("Enter transaction number to edit: ")

            if not idx_input.isdigit():
                print(" Invalid input")
                continue

            idx = int(idx_input) - 1

            new_amount = get_valid_amount()
            new_category = handle_category(VALID_CATEGORIES)

            confirm = input("Confirm update? (y/n): ").lower()
            if confirm == "y":
                if edit_expense(idx, {
                    "amount": new_amount,
                    "category": new_category
                }):
                    print(" Transaction updated successfully.")
                else:
                    print(" Invalid transaction number.")
            else:
                print(" Update cancelled.")

        except Exception as e:
            print("Error while updating:", e)

    elif choice == "4":
        data = get_all_expenses()
        display_expenses(data)

        try:
            idx_input = input("Enter transaction number to delete: ")

            if not idx_input.isdigit():
                print(" Invalid input")
                continue

            idx = int(idx_input) - 1

            confirm = input("Are you sure you want to delete? (y/n): ").lower()
            if confirm == "y":
                removed = delete_expense(idx)
                if removed:
                    print(f" Deleted: {removed}")
                else:
                    print(" Invalid transaction number.")
            else:
                print(" Deletion cancelled.")

        except Exception as e:  
            print("Error while deleting:", e)  

    elif choice == "5":
        month = input("Enter month (YYYY-MM): ")
        data = get_all_expenses()

        total, avg = monthly_summary(data, month)

        print(f"\n Monthly Summary ({month})")
        print(f"Total Expense: ₹{total}")
        print(f"Average per day: ₹{round(avg, 2)}")

    elif choice == "6":
        month = input("Enter month (YYYY-MM): ")
        data = get_all_expenses()

        summary = category_summary(data, month)
        show_advanced_chart(summary, month)

    elif choice == "7":
        print("Exiting... Goodbye 👋")
        break

    else:
        print("Invalid option. Please choose between 1–7.")
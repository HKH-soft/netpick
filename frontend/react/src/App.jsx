import SidebarDark from "./components/my-components/SidebarDark"
import { useEffect , useState } from "react"
import { getCustomers } from "./services/client"
import Card from "./components/my-components/Card"
import Modal from "./components/my-components/Modal"
import Notification from "./components/my-components/Notification"

function App() {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const notificationList = [];

  const fetchCustomers = () => {
    setLoading(true)
    getCustomers().then(res => {
      setCustomers(res.data)
    }).catch(err => {
      console.log(err)
    }).finally(() => {
      setLoading(false)
    })
  }
  useEffect(() => {
    fetchCustomers();
  },[])

  if(loading){
    return (
      <div className="flex h-screen w-screen relative">
        {/* Sidebar */}
        <div className="w-80 bg-gray-900 shrink-0">
          <SidebarDark />
        </div>

        {/* Main content */}
        <main className="h-full w-full bg-white text-black p-6">
          {/* Your main content here */}
        </main>

        {/* Fullscreen blurred loading overlay */}
        <div className="fixed inset-0 z-50 backdrop-blur-sm bg-black/40 flex items-center justify-center">
          <div
            className="w-12 h-12 rounded-full animate-spin border-4 border-solid border-gray-900 border-t-transparent"
            aria-label="Loading"
          ></div>
        </div>
      </div>

    )
  }
  
  if(customers.length <= 0){
    return (
    <div className="flex h-screen w-screen">
     {/* Sidebar */}
      <SidebarDark />

      {/* Main Content */}
      <main className="ml-80 flex-1 min-h-screen bg-white text-black p-6 overflow-y-auto">
        <div className="flex flex-col gap-y-4">
          <Modal fetchCustomers={fetchCustomers}></Modal>      
          <h1>no customer was found</h1>
        </div>
      </main>
    </div>

    )
  }

  return (
    <div className="flex h-screen w-screen">
     {/* Sidebar */}
      <SidebarDark />

      {/* Main Content */}
      <main className="ml-80 flex-1 min-h-screen bg-white text-black p-6 overflow-y-auto">
        <div className="mt-6 grid grid-cols-1 gap-x-8 gap-y-8 sm:grid-cols-2 sm:gap-y-10 lg:grid-cols-4">
          <Modal fetchCustomers={fetchCustomers}></Modal>
          {customers.map((customer, index) => (
            <Card
              key={index}
              id={customer.name}
              name={customer.name}
              category={customer.email}
              href={customer.name}
              price={customer.age}
              imageSrc={customer.gender == false? `https://randomuser.me/api/portraits/women/${index + 1}.jpg` : `https://randomuser.me/api/portraits/men/${index + 1}.jpg`}
              imageAlt={""}
            />
          ))}       
        </div>
      </main>

      {/* Global notification live region, render this permanently at the end of the document */}
      <div
        aria-live="assertive"
        className="pointer-events-none fixed inset-0 flex items-end px-4 py-6 sm:items-start sm:p-6"
      >
        <div className="flex w-full flex-col items-center space-y-4 sm:items-end">
          <Notification/>
        </div>
      </div>
    </div>
  )
}

export default App
